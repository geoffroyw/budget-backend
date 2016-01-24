class AddAccountIdToTransaction < ActiveRecord::Migration[5.0]
  def change
    add_column :transactions, :bank_account_id, :integer, :nullable => false
  end
end
